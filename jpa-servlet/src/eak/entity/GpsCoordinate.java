package eak.entity;

import java.io.*;
import java.util.Date;
import javax.persistence.*;

@Entity
public class GpsCoordinate implements Serializable {
  /**
	 * 
	 */
	private static final long serialVersionUID = -8747512517152809746L;
@Id @GeneratedValue 
  private long id;
  @Temporal(TemporalType.TIMESTAMP)
  private Date time;
  private double latitude;
  private double longitude;
  private double height;
  @ManyToOne
  @JoinColumn(name="coordinates")
  private Person owner;

  public GpsCoordinate(){}

  public GpsCoordinate(long id, Date time, double latitude, double longitude, double height, Person owner){
    this.id = id;
    this.time = time;
    this.latitude = latitude;
    this.longitude = longitude;
    this.height = height;
    this.owner = owner;
  }

  public long getId(){
    return this.id;
  }

  public Date getTime(){
    return this.time;
  }

  public double getLatitude(){
    return this.latitude;
  }

  public double getLongitude(){
    return this.longitude;
  }

  public double getHeight(){
    return this.height;
  }

  public Person getOwner(){
    return this.owner;
  }

  public void setId(long id){
    this.id = id;
  }

  public void setTime(Date time){
    this.time = time;
  }

  public void setLatitude(double d){
    this.latitude = d;
  }

  public void setLongitude(double d){
    this.longitude = d;
  }

  public void setHeight(double d){
    this.height = d;
  }

  public void setOwner(Person d){
    this.owner = d;
  }

  @Override
  public boolean equals(Object o){

    if (o == null)
       return false;
    if (!(o instanceof GpsCoordinate))
      return false;

    GpsCoordinate other = (GpsCoordinate) o;
    if (this.id != other.id)
      return false;

    return true;
  }

  @Override
  public int hashCode(){
    return (int)this.id;
  }

} 
