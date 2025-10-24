package com.problems.splitwise;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Group {
    private final String groupId;
    private final String name;
    private final String description;
    private final List<User> members;

    public Group(String groupId, String name, String description) {
        this.groupId = groupId;
        this.name = name;
        this.description = description;
        this.members = new ArrayList<>();
    }

    public void addMember(User user) {
        members.add(user);
    }

    public void removeMember(User user) {
        members.remove(user);
    }
}
