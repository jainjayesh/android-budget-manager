package de.zainodis.balancemanager.persistence;

/**
 * The below types are the data types officially supported by SQLite:
 * http://www.sqlite.org/datatype3.html .
 * 
 * @author zainodis
 * 
 */
public enum SQLiteDatatypes {

   /** The value is a null value - rarely used. */
   NULL,
   /** A signed integer stored in 1 to 8 bytes depending on the value. */
   INTEGER,
   /** A floating point value stored as 8-byte IEEE. */
   REAL,
   /** A String, stored using the database's encoding */
   TEXT,
   /** A blob of data stored exactly as input (for instance byte[]). */
   BLOB;
}
