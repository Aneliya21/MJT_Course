package Labs.lab_03.src.semester;

import Labs.lab_03.src.UniversitySubject;
import Labs.lab_03.src.exception.CryToStudentsDepartmentException;
import Labs.lab_03.src.exception.InvalidSubjectRequirementsException;
import Labs.lab_03.src.plan.SemesterPlan;

public final class ComputerScienceSemesterPlanner extends AbstractSemesterPlanner {

    @Override
    public UniversitySubject[] calculateSubjectList(SemesterPlan semesterPlan)
        throws InvalidSubjectRequirementsException {

        checkSemesterPlan(semesterPlan);
        checkForDuplicateCategoriesInSubjectRequirements(semesterPlan.subjectRequirements());

        checkIfIsPossibleToReachCredits(semesterPlan);

        int resultSize = findCountOfNeededSubjects(semesterPlan);
        UniversitySubject[] result = new UniversitySubject[resultSize];

        int resultInd = 0;
        int currentCredits = 0;
        for (UniversitySubject subject : semesterPlan.subjects()) {
            if (currentCredits >= semesterPlan.minimalAmountOfCredits()) {
                return result;
            }
            currentCredits += subject.credits();
            result[resultInd++] = subject;
        }

        return null;
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

    private int findCountOfNeededSubjects(SemesterPlan semesterPlan)  {
        int currentCredits = 0;
        int size = 0;
        sortUniversitySubjects(semesterPlan.subjects());

        for (UniversitySubject subject : semesterPlan.subjects()) {
            if (currentCredits >= semesterPlan.minimalAmountOfCredits()) {
                return size;
            }
            currentCredits += subject.credits();
            size++;
        }

        throw new CryToStudentsDepartmentException("Could not reach the needed amount of credits");
    }

    private void sortUniversitySubjects(UniversitySubject[] subjects) {
        for (int i = 0; i < subjects.length - 1; i++) {
            int minInd = i;

            for (int j = i + 1; j < subjects.length; j++) {
                if (subjects[j].rating() > subjects[minInd].rating()) {
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
}
