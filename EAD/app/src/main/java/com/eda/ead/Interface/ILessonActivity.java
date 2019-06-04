package com.eda.ead.Interface;

import com.eda.ead.Model.Lesson;
import com.eda.ead.Model.Skill;

public interface ILessonActivity {

    void updateLesson(Lesson lesson, Skill skill);

    void onLessonSelected(Lesson lesson);

}
