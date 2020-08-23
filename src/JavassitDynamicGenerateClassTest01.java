import java.io.IOException;
import java.lang.reflect.Method;

import javassist.*;
import javassist.bytecode.AccessFlag;
import javassist.compiler.CompileError;
/*
 �ο�����:https://blog.csdn.net/ShuSheng0007/article/details/81269295
 https://blog.csdn.net/shuzhe66/article/details/39319121
 https://www.cnblogs.com/baiqiantao/p/10284449.html
 */
public class JavassitDynamicGenerateClassTest01 {
	
	/**
	 * ����Javassist����.class�ļ���
	 * */
	public static void dynGenerateClass01(){
		ClassPool pool = ClassPool.getDefault();
		
		CtClass ctClass= pool.makeClass("GenerateClass01");
		
		try{
			CtField fieldId=new CtField(CtClass.intType,"id",ctClass);
			fieldId.setModifiers(AccessFlag.PUBLIC);
			ctClass.addField(fieldId);
				
			CtField fieldName=new CtField(pool.get(String.class.getName()),"name",ctClass);
			fieldName.setModifiers(AccessFlag.PUBLIC);
			ctClass.addField(fieldName);
			
			CtConstructor ctConstructor= CtNewConstructor.make("public GenerateClass01(int id,String name){this.id=id;this.name=name;}",ctClass);
			ctClass.addConstructor(ctConstructor);
			
			CtMethod methodShowInfo=CtNewMethod.make("public void showInfo(){System.out.println(\"id=\"+id+\",name=\"+name);}", ctClass);
			ctClass.addMethod(methodShowInfo);
			ctClass.writeFile(".\\bin");
			
			Class cl= Class.forName("GenerateClass01");
			Object obj= cl.getConstructor(new Class[]{int.class,String.class}).newInstance(new Object[]{Integer.valueOf(101),"zhangsan"});
			Method method=cl.getMethod("showInfo", null);
			method.invoke(obj, null);
			
		} catch (CannotCompileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	/**
	 * �޸����еķ���
	 * @param originFilePath ԭʼ�ļ�·����ͨ��Ϊ.jar�ļ��ļ���·��������Ϊnull��
	 * @param className	��Ҫ�޸ĵ�����
	 * @param methodName	��Ҫ�޸ķ���������
	 * @param paramsType	������������
	 * @param modifyContent	�������޸ĺ������
	 * @param savePath	����Ŀ���ļ�����·��
	 * */
	public static void modifyMethod(String originFilePath,String className,String methodName,String[] paramsType,String modifyContent,String savePath){
		ClassPool pool=ClassPool.getDefault();	
		try {
			pool.insertClassPath(originFilePath);/*����ָ��·���µ�.jar*/
			CtClass ctClass= pool.get(className);/*����ָ������*/
			
			CtClass[] paramsClass=null;
			if(paramsType!=null&&paramsType.length>0){
				paramsClass=new CtClass[paramsType.length];
				for(int i=0;i<paramsType.length;i++){
					paramsClass[i]=pool.get(paramsType[i]);
				}
			}
			
			CtMethod ctMethod=ctClass.getDeclaredMethod(methodName,paramsClass);
			
			ctMethod.setBody(modifyContent);
			
			ctClass.writeFile(savePath);
					
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		dynGenerateClass01();
		
		String modifyContent="{\n"
		+"System.out.println(\"currentTime=\"+System.currentTimeMillis());\n"
		+"System.out.println(\"showInfo\");\n}";
		
		modifyMethod(".\\src\\Student.java",Student.class.getName(),"showInfo",null,modifyContent,".\\bin");
	}

}
