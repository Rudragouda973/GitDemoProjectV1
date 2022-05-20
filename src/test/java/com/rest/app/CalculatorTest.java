package com.rest.app;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import com.rest.app.Calculator;

public class CalculatorTest {

  Calculator calculator;
  
  @BeforeEach
  public void setUp() {
	  calculator =  new Calculator();
  }
  
  @Test 
  @DisplayName("Simple multiplication should work")
  public void testMultiply() {
	  assertEquals(20, calculator.multiply(4, 5));
	  assertEquals(25, calculator.multiply(5, 5));
  }
  
  @RepeatedTest(5)                                    
  @DisplayName("Ensure correct handling of zero")
  void testMultiplyWithZero() {
      assertEquals(0, calculator.multiply(0, 5), "Multiple with zero should be zero");
      assertEquals(0, calculator.multiply(5, 0), "Multiple with zero should be zero");
  }
  
  @Test 
  public void testDivide() {
	  assertEquals(2, calculator.divide(4, 0));
  }
  
	/*
	 * @Test public void testMultiplyDiffNumbers() { assertEquals(25,
	 * calculator.multiply(5, 5)); }
	 */
	
}
