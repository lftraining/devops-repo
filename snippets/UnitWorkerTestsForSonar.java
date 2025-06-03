package worker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import worker.Worker;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
 
class UnitWorker {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    void testFizzBuzzGenerate() {
        // Test with limit = 5
        Worker.FizzBuzz.generate(5);
        Assertions.assertEquals("1\n2\nFizz\n4\nBuzz\n", outContent.toString());
        outContent.reset(); // Reset the stream for the next test

        // Test with limit = 15
        Worker.FizzBuzz.generate(15);
        Assertions.assertEquals("1\n2\nFizz\n4\nBuzz\nFizz\n7\n8\nFizz\nBuzz\n11\nFizz\n13\n14\nFizzBuzz\n", outContent.toString());
        outContent.reset();

        // Test with limit = 0
        Worker.FizzBuzz.generate(0);
        Assertions.assertEquals("", outContent.toString());
        outContent.reset();

        // Test with limit = 1
        Worker.FizzBuzz.generate(1);
        Assertions.assertEquals("1\n", outContent.toString());
        outContent.reset();
    }
 
    @Test
    void sample1() {
 
    }


    @Test
    void sample2() {

    }

    @Test
    void sample3() {

    }

    @Test
    void sample4() {

    }
   @Test
    void sample5() {

    }  
    @Test
    void sample6() {

    }  
    @Test
    void sample7() {

    }  
    @Test
    void sample8() {

    }  
    @Test
    void sample9() {

    }  
    @Test
    void sample10() {

    }  
    @Test
    void sample16() {

    }  
     @Test
    void sample17() {

    }  

 @Test
    void sample18() {

    }  
 @Test
    void sample19() {

    }  
  @Test
    void sample20() {

    }  
}



