package mates;

/** 
  *  ========================================================
  *  Vector2D.java: Source code for two-dimensional vectors
  * 
  *  Written by: Mark Austin                   November, 2005
  *  ========================================================
  */

public class Vector2D {

   protected double dX;
   protected double dY;

   // Constructor methods ....

   public Vector2D() {
      dX = dY = 0.0;
   }

   public Vector2D( double dX, double dY ) {
      this.dX = dX;
      this.dY = dY;
   }

   // Convert vector to a string ...
    
   public String toString() {
      return "Vector2D(" + dX + ", " + dY + ")";
   }

   // Compute magnitude of vector ....
 
   public double length() {
      return Math.sqrt ( dX*dX + dY*dY );
   }

   // Sum of two vectors ....

   public Vector2D add( Vector2D v1 ) {
       Vector2D v2 = new Vector2D( this.dX + v1.dX, this.dY + v1.dY );
       return v2;
   }

   // Subtract vector v1 from v .....

   public Vector2D sub( Vector2D v1 ) {
       Vector2D v2 = new Vector2D( this.dX - v1.dX, this.dY - v1.dY );
       return v2;
   }

   // Scale vector by a constant ...

   public Vector2D scale( double scaleFactor ) {
       Vector2D v2 = new Vector2D( this.dX*scaleFactor, this.dY*scaleFactor );
       return v2;
   }

   // Normalize a vectors length....

   public Vector2D normalize() {
      Vector2D v2 = new Vector2D();

      double length = Math.sqrt( this.dX*this.dX + this.dY*this.dY );
      if (length != 0) {
        v2.dX = this.dX/length;
        v2.dY = this.dY/length;
      }

      return v2;
   }   

   // Dot product of two vectors .....

   public double dotProduct ( Vector2D v1 ) {
        return this.dX*v1.dX + this.dY*v1.dY;
   }
   
   @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(dX);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(dY);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vector2D other = (Vector2D) obj;
		if (Double.doubleToLongBits(dX) != Double.doubleToLongBits(other.dX))
			return false;
		if (Double.doubleToLongBits(dY) != Double.doubleToLongBits(other.dY))
			return false;
		return true;
	}

	public void setLocation(double x, double y) {
	   dX = x;
	   dY = y;
   }

   public double getX() {
	   return dX;
   }
   
   public double getY() {
	   return dY;
   }
}