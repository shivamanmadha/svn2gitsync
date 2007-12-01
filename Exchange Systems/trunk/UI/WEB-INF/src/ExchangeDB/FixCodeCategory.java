package ExchangeDB;

public class FixCodeCategory {

  private int id;
  private String description;
  private String createdBy;
  private String creationDate;
  private String lastModifiedBy;
  private String lastModifiedDate;
  
  public void setId(int id)
  {
    this.id = id;
  }
  
  public int getId()
  {
    return this.id;
  }

  public void setDescription(String description)
  {
    this.description = description;
  }
  
  public String getDescription()
  {
	return description;
  }

  public void setCreatedBy(String createdBy)
  {
    this.createdBy = createdBy;
  }

  public String getCreatedBy()
  {
    return createdBy;
  }

  public void setCreationDate(String creationDate)
  {
    this.creationDate = creationDate;
  }

  public String getCreationDate()
  {
    return creationDate;
  }

  public void setLastModifiedBy(String lastModifiedBy)
  {
    this.lastModifiedBy = lastModifiedBy;
  }

  public String getLastModifiedBy()
  {
    return lastModifiedBy;
  }

  public void setLastModifiedDate(String lastModifiedDate)
  {
    this.lastModifiedDate = lastModifiedDate;
  }

  public String getLastModifiedDate()
  {
    return lastModifiedDate;
  } 
} 