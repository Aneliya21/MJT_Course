package bg.sofia.uni.fmi.mjt.pipeline.stage;

import bg.sofia.uni.fmi.mjt.pipeline.step.Step;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class StageTest {

    private Step<String, Integer> step1;
    private Step<Integer, Double> step2;
    private Step<Double, String> step3;

    @BeforeEach
    void setUp() {
        step1 = mock(Step.class);
        step2 = mock(Step.class);
        step3 = mock(Step.class);
    }

    @Test
    void testStartWithNullStepThrows() {
        assertThrows(IllegalArgumentException.class, () -> Stage.start(null),
            "IllegalArgumentException should be thrown");
    }

    @Test
    void testStartWithValidStepCreatesStage() {
        Stage<String, Integer> stage = Stage.start(step1);
        assertNotNull(stage, "Stage.start should return a non-null stage");
    }

    @Test
    void testAddStepWithNullStepThrows() {
        Stage<String, Integer> stage = Stage.start(step1);
        assertThrows(IllegalArgumentException.class, () -> stage.addStep(null),
            "IllegalArgumentException should be thrown");
    }

    @Test
    void testAddStepReturnsNewStageWithUpdatedOutputType() {
        Stage<String, Integer> stage1 = Stage.start(step1);
        Stage<String, Double> stage2 = stage1.addStep(step2);

        assertNotSame(stage1, stage2, "addStep should return a new stage instance");
    }

    @Test
    void testExecuteSingleStep() {
        Stage<String, Integer> stage = Stage.start(step1);
        when(step1.process("input")).thenReturn(42);

        Integer result = stage.execute("input");
        assertEquals(42, result, "execute should return the output of the single step");
    }

    @Test
    void testExecuteMultipleSteps() {
        Stage<String, Double> stage = Stage.start(step1)
            .addStep(step2);

        when(step1.process("input")).thenReturn(10);
        when(step2.process(10)).thenReturn(3.14);

        Double result = stage.execute("input");
        assertEquals(3.14, result, "execute should correctly chain multiple steps");
    }

    @Test
    void testExecuteWithThreeSteps() {
        Stage<String, String> stage = Stage.start(step1)
            .addStep(step2)
            .addStep(step3);

        when(step1.process("abc")).thenReturn(5);
        when(step2.process(5)).thenReturn(2.5);
        when(step3.process(2.5)).thenReturn("DONE");

        String result = stage.execute("abc");
        assertEquals("DONE", result, "execute should correctly chain three steps");
    }

    @Test
    void testExecuteWithNullInput() {
        Stage<String, Integer> stage = Stage.start(step1);
        when(step1.process(null)).thenReturn(0);

        Integer result = stage.execute(null);
        assertEquals(0, result, "execute should handle null input if step supports it");
    }
}
