package ExchangeDB
       {
              [Managed]
              [RemoteClass(alias="ExchangeDB.FixCodeCategory")]
              public class FixCodeCategory
              {
                     public function FixCodeCategory() {}
                     
                     public var id:int;
                     public var description:String="";
                     public var createdBy:String="";
					 public var creationDate:String="";
					 public var lastModifiedBy:String="";
					 public var lastModifiedDate:String="";					 
              }
       } 