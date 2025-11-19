package common;

public class PropertyValue {
  
  private Double marketValue;
  private Double totalLivableArea;
  private String zipCode;

  public PropertyValue(Double marketValue, Double totalLivableArea, String zipCode) {
    this.marketValue = marketValue;
    this.totalLivableArea = totalLivableArea;
    this.zipCode = zipCode;
  }

  public Double getMarketValue() {
    return marketValue;
  }

  public void setMarketValue(Double marketValue) {
    this.marketValue = marketValue;
  }

  public Double getTotalLivableArea() {
    return totalLivableArea;
  }

  public void setTotalLivableArea(Double totalLivableArea) {
    this.totalLivableArea = totalLivableArea;
  }

  public String getZipCode() {
    return zipCode;
  }

  public void setZipCode(String zipCode) {
    this.zipCode = zipCode;
  }

  @Override
  public String toString() {
    return "PropertyValue{" +
            "marketValue=" + marketValue +
            ", totalLivableArea=" + totalLivableArea +
            ", zipCode='" + zipCode + '\'' +
            '}';
  }
}
