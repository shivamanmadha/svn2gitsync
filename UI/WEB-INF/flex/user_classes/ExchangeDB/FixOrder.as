package ExchangeDB
       {
              [Managed]
              [RemoteClass(alias="ExchangeDB.FixOrder")]
              public class FixOrder
              {
                     public function FixOrder() {}
                     
                     public var id:int;
                     public var account:String="";
                     public var clorderid:String="";
					 public var ordQty:String="";
					 public var ordType:String="";
					 public var price:String="";
					 public var side:String="";
					 public var symbol:String="";
					 public var tif:String="";
					 public var securityDesc:String="";
					 public var securityType:String="";					 
              }
       } 