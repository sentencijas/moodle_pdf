package by.sentencija.entity;

import java.util.List;
import java.util.Map;

public record MoodleCourse(
        Course course,
        Map<String, PluginFile> fileMap,
        List<Section> sections
) { }