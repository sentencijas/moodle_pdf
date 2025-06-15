package by.sentencija.entity;

import java.util.List;

public record MoodleCourse(
        Course course,
        List<Section> sections
) { }