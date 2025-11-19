package common;

public class Population {

  private String zipCode;
  private int population;

  public Population(String zipCode, int population) {
    this.zipCode = zipCode;
    this.population = population;
  }

  public String getZipCode() {
    return zipCode;
  }

  public void setZipCode(String zipCode) {
    this.zipCode = zipCode;
  }

  public int getPopulation() {
    return population;
  }

  public void setPopulation(int population) {
    this.population = population;
  }
}
