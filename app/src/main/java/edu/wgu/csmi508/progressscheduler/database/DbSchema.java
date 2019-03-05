package edu.wgu.csmi508.progressscheduler.database;

public class DbSchema {
    public static final class TermTable {
        public static final String NAME = "term";
        public static final String CREATE = "create table " + NAME + "(" +
                " _id integer primary key autoincrement, " +
                Cols.UUID + ", " +
                Cols.TERM_NAME + ", " +
                Cols.START_DATE + ", " +
                Cols.END_DATE + ")";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String TERM_NAME = "term_name";
            public static final String START_DATE = "start_date";
            public static final String END_DATE = "end_date";
        }
    }

    public static final class CourseTable {
        public static final String NAME = "course";
        public static final String CREATE = "create table " + NAME + "(" +
                " _id integer primary key autoincrement, " +
                Cols.UUID + ", " +
                Cols.COURSE_CODE + ", " +
                Cols.COURSE_NAME + ", " +
                Cols.START_DATE + ", " +
                Cols.END_DATE + ", " +
                Cols.STATUS + ", " +
                Cols.START_ALARM + ", " +
                Cols.END_ALARM + ")";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String COURSE_CODE = "course_code";
            public static final String COURSE_NAME = "course_name";
            public static final String START_DATE = "start_date";
            public static final String END_DATE = "end_date";
            public static final String STATUS = "status";
            public static final String START_ALARM = "start_alarm";
            public static final String END_ALARM = "end_alarm";
        }
    }

    public static final class MentorTable {
        public static final String NAME = "mentor";
        public static final String CREATE = "create table " + NAME + "(" +
                " _id integer primary key autoincrement, " +
                Cols.UUID + ", " +
                Cols.NAME + ", " +
                Cols.PHONE + ", " +
                Cols.EMAIL + ")";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String NAME = "name";
            public static final String PHONE = "phone";
            public static final String EMAIL = "email";
        }
    }

    public static final class AssessmentTable {
        public static final String NAME = "assessment";
        public static final String CREATE = "create table " + NAME + "(" +
                " _id integer primary key autoincrement, " +
                Cols.ASSESSMENT_UUID + ", " +
                Cols.COURSE_UUID + ", " +
                Cols.ASSESSMENT_NAME + ", " +
                Cols.P_OR_O + ", " +
                Cols.DATE + ", " +
                Cols.ALARM + ")";

        public static final class Cols {
            public static final String ASSESSMENT_UUID = "assessment_uuid";
            public static final String COURSE_UUID = "course_uuid";
            public static final String ASSESSMENT_NAME = "assessment_name";
            public static final String P_OR_O = "performance_or_objective";
            public static final String DATE = "date";
            public static final String ALARM = "alarm";
        }
    }

    public static final class NoteTable {
        public static final String NAME = "note";
        public static final String CREATE = "create table " + NAME + "(" +
                " _id integer primary key autoincrement, " +
                Cols.NOTE_UUID + ", " +
                Cols.COURSE_UUID + ", " +
                Cols.NOTE + ", " +
                Cols.TIMESTAMP + ")";

        public static final class Cols {
            public static final String NOTE_UUID = "note_uuid";
            public static final String COURSE_UUID = "course_uuid";
            public static final String NOTE = "note_string";
            public static final String TIMESTAMP = "timestamp";
        }
    }

    // ********** //

    public static final class TermCourseTable {
        public static final String NAME = "term_course";
        public static final String CREATE = "create table " + NAME + "(" +
                " _id integer primary key autoincrement, " +
                Cols.TERM + ", " +
                Cols.COURSE + ")";

        public static final class Cols {
            public static final String TERM = "term";
            public static final String COURSE = "course";
        }
    }

    public static final class CourseMentorTable {
        public static final String NAME = "course_mentor";
        public static final String CREATE = "create table " + NAME + "(" +
                " _id integer primary key autoincrement, " +
                Cols.COURSE + ", " +
                Cols.MENTOR + ")";

        public static final class Cols {
            public static final String COURSE = "course";
            public static final String MENTOR = "mentor";
        }
    }

//    public static final class CourseAssessment {
//        public static final String NAME = "course_assignment";
//        public static final String CREATE = "create table " + NAME + "(" +
//                " _id integer primary key autoincrement, " +
//                Cols.COURSE + ", " +
//                Cols.ASSESSMENT + ")";
//
//        public static final class Cols {
//            public static final String COURSE = "course";
//            public static final String ASSESSMENT = "assessment";
//        }
//    }

}
