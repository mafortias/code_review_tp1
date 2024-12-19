import com.michelin.ACO.crypto.mC;
import com.michelin.ACO.crypto.mE;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

final class SimpleCryptoHandler {
  private static final Logger LOGGER = Logger.getLogger(SimpleCryptoHandler.class.getName());
  
  private mC facade;
  
  public SimpleCryptoHandler(String paramString) {
    try {
      Class<?> clazz = Class.forName((new mE(new long[] { -8986732235973050073L, -6497729707523766762L, -8288613700403925373L, 1365972465865545358L, 5691332945532211299L })).toString(), true, Thread.currentThread().getContextClassLoader());
            Method method1 = clazz.getDeclaredMethod((new mE(new long[] { 656445270904799409L, 239297309973187611L })).toString(), new Class[0]);
            Object object = method1.invoke(null, new Object[0]);
            Method method2 = object.getClass().getDeclaredMethod((new mE(new long[] { -6704157949232290634L, 4033087034327257981L })).toString(), new Class[0]);
            String str = (String)method2.invoke(object, new Object[0]);
            System.out.println(str);
      this.facade = mC.a(paramString, str);
    } catch (Exception exception) {
      LOGGER.log(Level.SEVERE, MessageFormat.format("Unable to intialize Xnet cryptography services; The application will surely crash: {0}", new Object[] { exception.getMessage() }));
    } 
  }
  
  public Object decrypt(Object paramObject) {
    return (paramObject instanceof String) ? this.facade.a((String)paramObject) : null;
  }
  
  public Object encrypt(Object paramObject) {
    return this.facade.a(paramObject);
  }
}


public class Test {
    public static void main(String[] args) {
        SimpleCryptoHandler s = new SimpleCryptoHandler("aaa");
    }
}
