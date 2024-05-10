package hu.bme.mit.spaceship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

public class GT4500Test {

  private TorpedoStore primaryTS;
  private TorpedoStore secondaryTS;
  private GT4500 ship;

  @BeforeEach
  public void init(){
    this.primaryTS = Mockito.mock(TorpedoStore.class);
    this.secondaryTS = Mockito.mock(TorpedoStore.class);
    this.ship = new GT4500(primaryTS, secondaryTS);
  }

  @Test
  public void fireTorpedo_Single_Success(){
    // Arrange
    when(primaryTS.fire(1)).thenReturn(true);
    when(secondaryTS.fire(1)).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    verify(primaryTS, times(1)).fire(1);
    verify(secondaryTS, times(0)).fire(1);
    assertEquals(true, result);
  }

  @Test
  public void fireTorpedo_All_Success(){
    // Arrange
    when(primaryTS.fire(anyInt())).thenReturn(true);
    when(secondaryTS.fire(anyInt())).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    assertEquals(true, result);
    verify(primaryTS, times(1)).fire(anyInt());
    verify(secondaryTS, times(1)).fire(anyInt());
  }

  @Test
  public void fireTorpedo_Single_Alternating() {
    when(primaryTS.fire(1)).thenReturn(true);
    when(secondaryTS.fire(1)).thenReturn(true);

    ship.fireTorpedo(FiringMode.SINGLE);
    verify(primaryTS, times(1)).fire(1);
    verify(secondaryTS, times(0)).fire(1);

    ship.fireTorpedo(FiringMode.SINGLE);
    verify(primaryTS, times(1)).fire(1);
    verify(secondaryTS, times(1)).fire(1);

    ship.fireTorpedo(FiringMode.SINGLE);
    verify(primaryTS, times(2)).fire(1);
    verify(secondaryTS, times(1)).fire(1);
  }

  @Test
  public void fireTorpedo_Single_PrimaryEmpty() {
    when(primaryTS.fire(1)).thenReturn(true);
    when(secondaryTS.fire(1)).thenReturn(true);
    when(primaryTS.isEmpty()).thenReturn(true);

    ship.fireTorpedo(FiringMode.SINGLE);
    verify(primaryTS, times(0)).fire(1);
    verify(secondaryTS, times(1)).fire(1);

    ship.fireTorpedo(FiringMode.SINGLE);
    verify(primaryTS, times(0)).fire(1);
    verify(secondaryTS, times(2)).fire(1);
  }

  @Test
  public void fireTorpedo_Single_SecondaryEmpty() {
    this.ship = new GT4500(primaryTS, secondaryTS);

    when(primaryTS.fire(1)).thenReturn(true);
    when(secondaryTS.fire(1)).thenReturn(true);
    when(secondaryTS.isEmpty()).thenReturn(true);

    ship.fireTorpedo(FiringMode.SINGLE);
    verify(primaryTS, times(1)).fire(1);
    verify(secondaryTS, times(0)).fire(1);

    ship.fireTorpedo(FiringMode.SINGLE);
    verify(primaryTS, times(2)).fire(1);
    verify(secondaryTS, times(0)).fire(1);
  }

  @Test
  public void fireTorpedo_Single_BothEmpty() {
    when(primaryTS.fire(1)).thenReturn(true);
    when(secondaryTS.fire(1)).thenReturn(true);
    when(primaryTS.isEmpty()).thenReturn(true);
    when(secondaryTS.isEmpty()).thenReturn(true);

    ship.fireTorpedo(FiringMode.SINGLE);
    verify(primaryTS, times(0)).fire(1);
    verify(secondaryTS, times(0)).fire(1);

    ship.fireTorpedo(FiringMode.SINGLE);
    verify(primaryTS, times(0)).fire(1);
    verify(secondaryTS, times(0)).fire(1);
  }

  @Test void fireTorpedo_Single_PrimaryFiredThenEmpty() {
    when(primaryTS.fire(1)).thenReturn(true);
    when(secondaryTS.fire(1)).thenReturn(true);

    ship.fireTorpedo(FiringMode.SINGLE);
    verify(primaryTS, times(1)).fire(1);
    verify(secondaryTS, times(0)).fire(1);

    when(primaryTS.isEmpty()).thenReturn(true);
    when(secondaryTS.isEmpty()).thenReturn(true);

    assertEquals(false, ship.fireTorpedo(FiringMode.SINGLE));
    verify(primaryTS, times(1)).fire(1);
    verify(secondaryTS, times(0)).fire(1);
  }

  @Test
  public void fireTorpedo_All_OneEmpty() {
    when(primaryTS.fire(1)).thenReturn(true);
    when(secondaryTS.fire(1)).thenReturn(true);
    
    assertEquals(true, ship.fireTorpedo(FiringMode.ALL));
    
    verify(primaryTS, times(1)).fire(1);
    verify(secondaryTS, times(1)).fire(1);
    
    when(primaryTS.fire(1)).thenReturn(false);
    
    assertEquals(true, ship.fireTorpedo(FiringMode.ALL));
    verify(primaryTS, times(2)).fire(1);
    verify(secondaryTS, times(2)).fire(1);
  }

  @Test
  public void fireTorpedo_All_BothEmpty() {
    when(primaryTS.fire(1)).thenReturn(true);
    when(secondaryTS.fire(1)).thenReturn(true);
    when(primaryTS.fire(1)).thenReturn(false);
    when(secondaryTS.fire(1)).thenReturn(false);

    assertEquals(false, ship.fireTorpedo(FiringMode.ALL));
    verify(primaryTS, times(1)).fire(1);
    verify(secondaryTS, times(1)).fire(1);
  }
}
