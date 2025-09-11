Some problems to practice lld and implementing it for interviewing process. My solutions to the problems here is on my first trial. There is possibility of improvement in design and patterns I've used.


## DOCTOR APPOINTMENT PROBLEM

Problem : [problem.md](src/main/java/com/problems/docAppointment/problem.md)

Solution :
```
┌─────────────────────────┐    ┌──────────────────────────────┐    ┌─────────────────────────┐
│       TimeSlot          │    │          Doctor              │    │        Patient          │
├─────────────────────────┤    ├──────────────────────────────┤    ├─────────────────────────┤
│ - startTime: LocalTime  │    │ - doctorId: String           │    │ - patientId: String     │
│ - endTime: LocalTime    │    │ - name: String               │    │ - name: String          │
└─────────────────────────┘    │ - speciality: Speciality     │    └─────────────────────────┘
                               │ - availableTimeSlots: Set    │              │
                               │ - rating: Double             │              │
                               │ - totalAppointments: Integer │              │
                               ├──────────────────────────────┤              │
                               │ + addAvailableTimeSlot()     │              │
                               │ + isAvailableTimeSlot()      │              │
                               │ + incrementAppointments()    │              │
                               │ + decrementAppointments()    │              │
                               └──────────────────────────────┘              │
                                        │                                    │
                                        └─────────────┬──────────────────────┘
                                                      │
                            ┌─────────────────────────┼─────────────────────────┐
                            │                         │                         │
                    ┌───────────────────────┐  ┌──────────────────────┐  ┌─────────────────────────┐
                    │     Appointment       │  │      WaitList        │  │    AppointmentStatus    │
                    ├───────────────────────┤  ├──────────────────────┤  ├─────────────────────────┤
                    │ - appointmentId       │  │ - waitListId         │  │ <<enumeration>>         │
                    │ - patient: Patient    │  │ - patient: Patient   │  │ - BOOKED                │
                    │ - doctor: Doctor      │  │ - doctor: Doctor     │  │ - CANCELLED             │
                    │ - status: Status      │  │ - timeSlot: TimeSlot │  │ - COMPLETED             │
                    │ - timeSlot: TimeSlot  │  │ - createdAt: Date    │  └─────────────────────────┘
                    │ - createdAt: Date     │  │ - isWaitListConfirmed│
                    │ - updatedAt: Date     │  └──────────────────────┘
                    ├───────────────────────┤
                    │ + cancel()            │
                    └───────────────────────┘
                            │
┌──────────────────────────────────────────────────────────────────────────────────────────────┐
│                         AppointmentSystemFacade                                                 │
├──────────────────────────────────────────────────────────────────────────────────────────────┤
│ - appointmentService: AppointmentService                                                        │
│ - doctorService: DoctorService                                                                  │
│ - patientService: PatientService                                                               │
├──────────────────────────────────────────────────────────────────────────────────────────────┤
│ + setRankingStrategy(SlotRankingStrategyHandler)                                               │
│ + showAvailableSlotsBySpeciality(Speciality): List<String>                                    │
│ + bookAppointment(Doctor, Patient, TimeSlot): String                                          │
│ + cancelAppointment(String): String                                                           │
│ + getPatientAppointments(Patient): List<String>                                               │
│ + getWaitlistAppointments(Patient): List<String>                                              │
│ + getDoctorAppointments(Doctor): List<String>                                                 │
│ + getTrendingDoctor(): String                                                                 │
│ + registerDoctor(String, Speciality): String                                                  │
│ + setAvailableSlot(String, TimeSlot...): String                                               │
│ + registerPatient(String): String                                                             │
└──────────────────────────────────────────────────────────────────────────────────────────────┘
                                           │
              ┌────────────────────────────┼────────────────────────────┐
              │                            │                            │
    ┌─────────────────────┐      ┌─────────────────────┐      ┌─────────────────────┐
    │   DoctorService     │      │ AppointmentService  │      │   PatientService    │
    ├─────────────────────┤      ├─────────────────────┤      ├─────────────────────┤
    │ - doctorRepository  │      │ - doctorRepository  │      │                     │
    ├─────────────────────┤      │ - patientRepository │      ├─────────────────────┤
    │ + registerDoctor()  │      │ - appointmentRepo   │      │ + registerPatient() │
    │ + setAvailableSlot()│      │ - waitListRepository│      └─────────────────────┘
    └─────────────────────┘      │ - rankingStrategy   │
                                 ├─────────────────────┤
                                 │ + setRankingStrategy│
                                 │ + showAvailableSlots│
                                 │ + bookAppointment() │
                                 │ + cancelAppointment │
                                 │ + getPatientAppts() │
                                 │ + getWaitlistAppts()│
                                 │ + getDoctorAppts()  │
                                 │ + getTrendingDoctor │
                                 └─────────────────────┘
                                           │
                              ┌───────────────────────────┐
                              │ SlotRankingStrategyHandler│
                              │     <<interface>>         │
                              ├───────────────────────────┤
                              │ + rankSlots(List<Appt>)   │
                              └───────────────────────────┘
                                           │
                          ┌────────────────┴────────────────┐
                          │                                 │
              ┌─────────────────────────┐       ┌─────────────────────────┐
              │ TimeBasedRankingHandler │       │RatingBasedRankingHandler│
              ├─────────────────────────┤       ├─────────────────────────┤
              │ + rankSlots()           │       │ + rankSlots()           │
              └─────────────────────────┘       └─────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────────────────────────┐
│                                Repository Layer                                                │
└─────────────────────────────────────────────────────────────────────────────────────────────┘

┌───────────────────────────┐    ┌───────────────────────────┐    ┌───────────────────────────┐
│   AppointmentRepository   │    │    DoctorRepository       │    │   PatientRepository       │
│     <<interface>>         │    │     <<interface>>         │    │     <<interface>>         │
├───────────────────────────┤    ├───────────────────────────┤    ├───────────────────────────┤
│ + saveAppointment(Appt)   │    │ + saveDoctor(Doctor)      │    │ + savePatient(Patient)    │
│ + findAppointmentById()   │    │ + findDoctorById()        │    │ + findPatientById()       │
│ + findApptByPatient()     │    │ + findDoctorByName()      │    └───────────────────────────┘
│ + findApptByDoctor()      │    │ + findDoctorBySpeciality()│              │
│ + findApptByDoctorSlot()  │    │ + getAllDoctors()         │              │
└───────────────────────────┘    └───────────────────────────┘              │
            │                                │                               │
            │                                │                               │
┌───────────────────────────┐    ┌───────────────────────────┐              │
│AppointmentRepositoryImpl  │    │  DoctorRepositoryImpl     │              │
├───────────────────────────┤    ├───────────────────────────┤              │
│ + saveAppointment()       │    │ + saveDoctor()            │              │
│ + findAppointmentById()   │    │ + findDoctorById()        │              │
│ + findApptByPatient()     │    │ + findDoctorByName()      │              │
│ + findApptByDoctor()      │    │ + findDoctorBySpeciality()│              │
│ + findApptByDoctorSlot()  │    │ + getAllDoctors()         │              │
└───────────────────────────┘    └───────────────────────────┘              │
                                                                             │
                                                                             │
┌───────────────────────────┐    ┌───────────────────────────┐              │
│   WaitListRepository      │    │   PatientRepositoryImpl   │◄─────────────┘
│     <<interface>>         │    ├───────────────────────────┤
├───────────────────────────┤    │ + savePatient()           │
│ + saveWaitList()          │    │ + findPatientById()       │
│ + findWaitListByPatient() │    └───────────────────────────┘
│ + findWaitListByDoctorSlot│
│ + findWaitListOrderBy...  │
└───────────────────────────┘
            │
            │
┌───────────────────────────┐    ┌───────────────────────────┐
│  WaitListRepositoryImpl   │    │    TimeSlotValidator      │
├───────────────────────────┤    ├───────────────────────────┤
│ + saveWaitList()          │    │ + validateTimeSlot()      │
│ + findWaitListByPatient() │    │   (LocalTime, LocalTime)  │
│ + findWaitListByDoctorSlot│    └───────────────────────────┘
│ + findWaitListOrderBy...  │
└───────────────────────────┘
```