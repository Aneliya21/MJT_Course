package bg.sofia.uni.fmi.mjt.burnout.semester;

import bg.sofia.uni.fmi.mjt.burnout.exception.DisappointmentException;
import bg.sofia.uni.fmi.mjt.burnout.exception.InvalidSubjectRequirementsException;
import bg.sofia.uni.fmi.mjt.burnout.plan.SemesterPlan;
import bg.sofia.uni.fmi.mjt.burnout.subject.Category;
import bg.sofia.uni.fmi.mjt.burnout.subject.SubjectRequirement;
import bg.sofia.uni.fmi.mjt.burnout.subject.UniversitySubject;

public abstract sealed class AbstractSemesterPlanner implements SemesterPlannerAPI
    permits ComputerScienceSemesterPlanner, SoftwareEngineeringSemesterPlanner {

    private static final int MAX_CATEGORIES = 4;
    private static final int JAR_SUPPLY = 5;

    @Override
    public int calculateJarCount(UniversitySubject[] subjects, int maximumSlackTime, int semesterDuration) {
        if (maximumSlackTime <= 0) {
            throw new IllegalArgumentException("Maximum slack time cannot be negative");
        }
        if (semesterDuration <= 0) {
            throw new IllegalArgumentException("Semester duration cannot be negative");
        }

        checkSubjects(subjects);
        checkMaximumSlackTime(maximumSlackTime);
        checkSemesterDuration(semesterDuration);

        int breakTime = calculateTimeForBreak(subjects);
        int studyTime = calculateTimeForStudy(subjects);
        if (breakTime > maximumSlackTime) {
            throw new DisappointmentException("Grandma has been disappointed");
        }

        int totalRequiredHours = breakTime + studyTime;
        int jars = (totalRequiredHours + JAR_SUPPLY - 1) / JAR_SUPPLY;

        if (totalRequiredHours > semesterDuration) {
            int extraHours = totalRequiredHours - semesterDuration;
            int extraJars = (extraHours + JAR_SUPPLY - 1) / JAR_SUPPLY;
            jars = (semesterDuration + JAR_SUPPLY - 1) / JAR_SUPPLY + extraJars;
        }

        return jars - 1;
    }

    protected void checkSemesterPlan(SemesterPlan semesterPlan) {
        if (semesterPlan == null) {
            throw new IllegalArgumentException("Semester plan cannot be null");
        }
    }

    protected void checkSubjects(UniversitySubject[] subjects) {
        if (subjects == null || subjects.length == 0) {
            throw new IllegalArgumentException("Subject cannot be null or empty");
        }
    }

    protected void checkMaximumSlackTime(int maximumSlackTime) {
        if (maximumSlackTime < 0) {
            throw new IllegalArgumentException("Maximum Slack Time cannot be negative");
        }
    }

    protected void checkSemesterDuration(int semesterDuration) {
        if (semesterDuration < 0) {
            throw new IllegalArgumentException("Semester duration cannot be negative");
        }
    }

    protected void checkForDuplicateCategoriesInSubjectRequirements(SubjectRequirement[] subjectRequirements)
        throws InvalidSubjectRequirementsException {

        boolean[] categories = new boolean[MAX_CATEGORIES];

        for (SubjectRequirement subjectRequirement : subjectRequirements) {
            switch (subjectRequirement.category()) {
                case Category.MATH -> checkForMathCategory(categories);
                case Category.PROGRAMMING -> checkForProgrammingCategory(categories);
                case Category.THEORY -> checkForTheoryCategory(categories);
                case Category.PRACTICAL -> checkForPracticalCategory(categories);
            }
        }
    }

    protected int calculateTimeForBreak(UniversitySubject[] subjects) {
        int timeForBreak = 0;
        for (UniversitySubject subject : subjects) {
            timeForBreak += subject.neededStudyTime() * subject.category().getCoef();
        }
        return timeForBreak;
    }

    protected int calculateTimeForStudy(UniversitySubject[] subjects) {
        if (subjects == null || subjects.length == 0) {
            return 0;
        }

        int studyTime = 0;
        for (UniversitySubject subject : subjects) {
            if (subject == null) continue;
            studyTime += subject.neededStudyTime();
        }
        return studyTime;
    }

    protected int getAllDaysNeededStudyTime(UniversitySubject[] subjects) {
        int allDays = 0;
        for (UniversitySubject subject : subjects) {
            allDays += subject.neededStudyTime();
        }
        return allDays;
    }

    private void checkForMathCategory(boolean[] categories) throws InvalidSubjectRequirementsException {
        if (!categories[Category.MATH.getValue()]) {
            categories[Category.MATH.getValue()] = true;
        } else {
            throw new InvalidSubjectRequirementsException("MATH Category duplication is forbidden");
        }
    }

    private void checkForProgrammingCategory(boolean[] categories) throws InvalidSubjectRequirementsException {
        if (!categories[Category.PROGRAMMING.getValue()]) {
            categories[Category.PROGRAMMING.getValue()] = true;
        } else {
            throw new InvalidSubjectRequirementsException("PROGRAMMING Category duplication is forbidden");
        }
    }

    private void checkForTheoryCategory(boolean[] categories) throws InvalidSubjectRequirementsException {
        if (!categories[Category.THEORY.getValue()]) {
            categories[Category.THEORY.getValue()] = true;
        } else {
            throw new InvalidSubjectRequirementsException("THEORY Category duplication is forbidden");
        }
    }

    private void checkForPracticalCategory(boolean[] categories) throws InvalidSubjectRequirementsException {
        if (!categories[Category.PRACTICAL.getValue()]) {
            categories[Category.PRACTICAL.getValue()] = true;
        } else {
            throw new InvalidSubjectRequirementsException("PRACTICAL Category duplication is forbidden");
        }
    }
}

