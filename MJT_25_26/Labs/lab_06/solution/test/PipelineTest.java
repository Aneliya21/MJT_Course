package bg.sofia.uni.fmi.mjt.pipeline;

import bg.sofia.uni.fmi.mjt.pipeline.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PipelineTest {

    private Stage<String, Integer> stage1;
    private Stage<Integer, Double> stage2;
    private Pipeline<String, Integer> pipeline;

    @BeforeEach
    void setUp() {
        stage1 = mock(Stage.class);
        stage2 = mock(Stage.class);

        pipeline = Pipeline.start(stage1);
    }

    @Test
    void testStartWithNullStageThrows() {
        assertThrows(IllegalArgumentException.class, () -> Pipeline.start(null),
            "Pipeline.start should throw IllegalArgumentException if initial stage is null");
    }

    @Test
    void testStartWithValidStageReturnsPipeline() {
        Pipeline<String, Integer> p = Pipeline.start(stage1);
        assertNotNull(p, "Pipeline.start should return a non-null instance for valid stage");
    }

    @Test
    void testAddStageReturnsPipelineWithUpdatedOutputType() {
        Pipeline<String, Double> newPipeline = pipeline.addStage(stage2);
        assertNotNull(newPipeline, "addStage should return a new pipeline instance");
    }

    @Test
    void testExecuteSingleStage() {
        when(stage1.execute("input")).thenReturn(42);

        Integer result = pipeline.execute("input");
        assertEquals(42, result, "execute should return output from the single stage");
    }

    @Test
    void testExecuteCachesResult() {
        when(stage1.execute("input")).thenReturn(42);

        Integer firstResult = pipeline.execute("input");
        Integer secondResult = pipeline.execute("input");

        verify(stage1, Mockito.times(1)).execute("input");
        assertEquals(firstResult, secondResult, "execute should return the cached result for repeated input");
    }

    @Test
    void testAddStageWithNullStageThrows() {
        pipeline.addStage((Stage) null);
        assertThrows(NullPointerException.class, () -> pipeline.execute("input"),
            "Executing pipeline with a null stage should throw an exception");
    }
}
