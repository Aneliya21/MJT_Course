package bg.sofia.uni.fmi.mjt.burnout.semester;

import bg.sofia.uni.fmi.mjt.burnout.exception.CryToStudentsDepartmentException;
import bg.sofia.uni.fmi.mjt.burnout.exception.InvalidSubjectRequirementsException;
import bg.sofia.uni.fmi.mjt.burnout.plan.SemesterPlan;
import bg.sofia.uni.fmi.mjt.burnout.subject.Category;
import bg.sofia.uni.fmi.mjt.burnout.subject.SubjectRequirement;
import bg.sofia.uni.fmi.mjt.burnout.subject.UniversitySubject;

public final class SoftwareEngineeringSemesterPlanner extends AbstractSemesterPlanner {

    private static final int CATEGORIES_MAX_COUNT = 4;

    @Override
    public UniversitySubject[] calculateSubjectList(SemesterPlan semesterPlan)
        throws InvalidSubjectRequirementsException {

        checkSemesterPlan(semesterPlan);
        checkForDuplicateCategoriesInSubjectRequirements(semesterPlan.subjectRequirements());
        checkIfIsPossibleToReachCredits(semesterPlan);

        sortUniversitySubject(semesterPlan.subjects());
        sortSubjectRequrements(semesterPlan.subjectRequirements());

        int resultSize = findCountOfNeededSubjects(semesterPlan.subjectRequirements());
        UniversitySubject[] result = new UniversitySubject[resultSize];

        int[] countOfSubjectsPerCategory = getCountOfSubjectsPerCategory(semesterPlan.subjects());
        int resultIndex = 0;
        for (SubjectRequirement subjectRequirement : semesterPlan.subjectRequirements()) {
            if (!canFulfillRequirement(subjectRequirement, countOfSubjectsPerCategory)) continue;

            resultIndex = fillSubjectsOfCategory(semesterPlan.subjects(), result,
                                                    resultIndex, subjectRequirement.category());

            if (resultIndex >= result.length) break;
        }

        if (resultIndex != result.length) {
            UniversitySubject[] trimmed = new UniversitySubject[resultIndex];
            System.arraycopy(result, 0, trimmed, 0, resultIndex);
            return trimmed;
        }
        return result;
    }

    private void checkIfIsPossibleToReachCredits(SemesterPlan semesterPlan) {
        int requiredCredits = semesterPlan.minimalAmountOfCredits();

        int totalPossibleCredits = 0;
        for (UniversitySubject s : semesterPlan.subjects()) {
            totalPossibleCredits += s.credits();
        }
        if (totalPossibleCredits < requiredCredits) {
            throw new CryToStudentsDepartmentException("Impossible to reach minimal credits");
        }
    }

    private boolean canFulfillRequirement(SubjectRequirement req, int[] countOfSubjectsPerCategory) {
        Category category = req.category();
        int required = req.minAmountEnrolled();

        if (countOfSubjectsPerCategory == null) {
            return false;
        }

        switch (category) {
            case MATH:
                return countOfSubjectsPerCategory[Category.MATH.getValue()] >= required;
            case PROGRAMMING:
                return countOfSubjectsPerCategory[Category.PROGRAMMING.getValue()] >= required;
            case THEORY:
                return countOfSubjectsPerCategory[Category.THEORY.getValue()] >= required;
            case PRACTICAL:
            default:
                return countOfSubjectsPerCategory[Category.PRACTICAL.getValue()] >= required;
        }
    }

    private int fillSubjectsOfCategory(UniversitySubject[] subjects,
                                       UniversitySubject[] result,
                                       int startIndex,
                                       Category category) {
        int writeIndex = startIndex;
        if (subjects == null || result == null || startIndex >= result.length) {
            return writeIndex;
        }

        for (UniversitySubject subject : subjects) {
            if (writeIndex >= result.length) break;
            if (subject == null) continue;
            if (subject.category() == category) {
                result[writeIndex++] = subject;
            }
        }
        return writeIndex;
    }

    private void fillSubjects(UniversitySubject[] subjects, UniversitySubject[] toFill, int index, Category category) {
        for (UniversitySubject subject : subjects) {
            if (subject.category() == category) {
                toFill[index++] = subject;
            }
        }
    }

    private int findCountOfNeededSubjects(SubjectRequirement[] subjectRequirements) {
        int result = 0;
        for (SubjectRequirement subjectRequirement : subjectRequirements) {
            result += subjectRequirement.minAmountEnrolled();
        }
        return result;
    }

    private void sortUniversitySubject(UniversitySubject[] subjects) {
        for (int i = 0; i < subjects.length - 1; i++) {
            int minInd = i;

            for (int j = i + 1; j < subjects.length; j++) {
                if (subjects[j].category().getValue() < subjects[minInd].category().getValue()) {
                    minInd = j;
                }
            }

            if (i != minInd) {
                UniversitySubject temp = subjects[i];
                subjects[i] = subjects[minInd];
                subjects[minInd] = temp;
            }
        }
    }

    private void sortSubjectRequrements(SubjectRequirement[] subjectRequirements) {
        for (int i = 0; i < subjectRequirements.length - 1; i++) {
            int minInd = i;

            for (int j = i + 1; j < subjectRequirements.length; j++) {
                if (subjectRequirements[j].category().getValue() < subjectRequirements[minInd].category().getValue()) {
                    minInd = j;
                }
            }

            if (i != minInd) {
                SubjectRequirement temp = subjectRequirements[i];
                subjectRequirements[i] = subjectRequirements[minInd];
                subjectRequirements[minInd] = temp;
            }
        }
    }

    private int[] getCountOfSubjectsPerCategory(UniversitySubject[] subjects) {
        int[] result = new int[CATEGORIES_MAX_COUNT];

        for (UniversitySubject subject : subjects) {

            if (subject.category() == Category.MATH) {
                result[Category.MATH.getValue()]++;

            } else if (subject.category() == Category.PROGRAMMING) {
                result[Category.PROGRAMMING.getValue()]++;

            } else if (subject.category() == Category.THEORY) {
                result[Category.THEORY.getValue()]++;

            } else {
                result[Category.PRACTICAL.getValue()]++;
            }
        }

        return result;
    }
}
