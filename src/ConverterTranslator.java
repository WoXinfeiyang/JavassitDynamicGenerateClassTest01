import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CodeConverter;
import javassist.CtClass;
import javassist.NotFoundException;


public class ConverterTranslator implements  javassist.Translator {
	private static final String TAG=ConverterTranslator.class.getSimpleName();
	
	private CodeConverter mCodeConverter;
	
	public void setCodeConverter(CodeConverter codeConverter){
		this.mCodeConverter=codeConverter;
	}
	
	public void start(ClassPool pool) throws NotFoundException,
			CannotCompileException {
			
	}

	public void onLoad(ClassPool pool, String classname)
			throws NotFoundException, CannotCompileException {
		System.out.println(TAG+":	pool="+pool+",classname="+classname);
		CtClass ctClass= pool.get(classname);
		ctClass.instrument(mCodeConverter);
	}

	

}
