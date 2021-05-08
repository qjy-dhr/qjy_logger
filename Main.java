package 表达式求值1;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.*;
class Stack{
	 int size;
	float[] s;
	int top=-1;
	Stack(int size){
		this.size=size;
		s=new float[size];
	}
	 void push(float v) {
		 s[++top]=v; 
	 }
	 float top() {
		return s[top]; 
	 }
	 void pop() {
		 top--;
	 }
	 Boolean empty() {
		 return top==-1;
	 } 
 } 
public class Main {
	static Boolean judge1(int a) {
		if(a=='+'||a=='-'||a=='*'||a=='/'||a=='%') {
			return true;
		}
		else return false;
	}
	static Boolean judge(int a) {
		if(a=='+'||a=='-'||a=='*'||a=='/'||a=='%'||a=='('||a==')') {
			return true;
		}
		else return false;
	}
	static int priority(int f) {
		if(f=='+'||f=='-') {
			return 1;
		}
		else if(f=='*'||f=='/'||f=='%') {
			return 2;
		}
		else if(f=='('){
			return -1;
		}
		else return 0;
	}

static void calculate(String expression,Boolean pan,Logger logger){
	
	int error=1;
	int zuo=0;
	int you=0;
	Stack numstack=new Stack(100);
	Stack operatorstack=new Stack(100);
	Stack num=new Stack(100);
	int index=0;
	char temp;
	float num1;
	float num2;
	int operate;
	Boolean tempq=false;
	String tempstring="";
	
	
	while(true) {
		Boolean tempbool=true;
		temp=expression.charAt(index);
		if((temp=='-'&&index==0)||(index>0&&expression.charAt(index-1)=='(')&&temp=='-') {
			tempbool=false;//解决负号问题
		}
		if(judge(temp)&&tempbool) {
			if(operatorstack.empty()||temp=='(') {
				if(temp=='(')
				{
					zuo++;
				}
				operatorstack.push(temp);	
			}
			else {
				if(priority(temp)>priority((int)operatorstack.top())||operatorstack.top()=='(') {
					operatorstack.push(temp);
				}
				else if(temp==')') {
					you++;
					
					try {
						while(operatorstack.top()!='('&&!operatorstack.empty()) {
						 num1=numstack.top();float n1=num.top();
							numstack.pop();num.pop();
							 num2=numstack.top();float n2=num.top();
							 numstack.pop();num.pop();
							 operate=(int) operatorstack.top();
							operatorstack.pop();
							float r;
							 if(n1==n2&&n1==0) {
								  
								 r=(int)result2((int)num1,(int)num2,operate);
							 num.push(0);
							 }
							 else {
								 r=result(num1,num2,operate);
								 num.push(1);
							 }
							 
							numstack.push(r);
							
						}if(operatorstack.top()=='(')
					operatorstack.pop();
					} catch (Exception e) {
						// TODO Auto-generated catch block
//						System.out.print("括号不匹配");
						logger.log(Level.INFO,"计算 "+expression+" 失败\n         原因：括号不匹配");
						error=0;
						break;
					}
				}
				else {
					int tempt=0;
					while(true) {
						if(operatorstack.empty()||priority(temp)>priority((int)operatorstack.top())) {
							break;
						}
				   num1=numstack.top();float n1=num.top();
				   
					numstack.pop();num.pop();
					
					 float n2;
					try {
						num2=numstack.top();n2 = num.top();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						error=0;
						logger.log(Level.INFO,"计算："+expression+" 失败\n         原因：两个运算符并列");
					  //解决两个操作符在一起
						tempt=1;
						break;
					}
					 
					 numstack.pop(); num.pop();
					
					 operate=(int)operatorstack.top();
					 operatorstack.pop();
					 float r;
					 if(n1==n2&&n1==0) {
						  r=(int)result2((int)num1,(int)num2,operate);
						  num.push(0);
					 }
					 
					 else { r=result(num1,num2,operate);
						 num.push(1);
					 }
					
					numstack.push(r);
					
				}
					if(tempt==1) {
						break;
					}
					operatorstack.push(temp);
					}
			}
		}
    else if(temp=='='){
			index++;
		}
	else if((temp>='0'&&temp<='9')||temp=='.'||temp=='-'){
			if(temp=='.') {
				tempq=true;
				tempstring+=temp;
				index++;
				continue;		
			}
			if(temp=='-') {
				tempstring+=temp;
				index++;
				continue;
			}
			tempstring+=temp;
			int c=expression.length();
			if(index==c-1||(index<c-1&&(judge(expression.charAt(index+1)))||expression.charAt(index+1)=='=')){
				if(tempq) {
					float v=Float.parseFloat(tempstring);
					numstack.push(v);
					num.push(1);
					tempq=false;
				}
				else {
					int v=Integer.parseInt(tempstring);
					numstack.push(v);
					num.push(0);
				}
				tempstring="";
			}						
		}
		else {
			error=0;
//			System.out.print("出现其他字符");
			logger.log(Level.INFO,"计算 "+expression+" 失败\n         原因：出现其他字符");
			break;
		}
		index++;
		if(index>=expression.length()) {
			break;
		}	
	}
	if(error==1) {
	
	while(true) {
		if(operatorstack.empty()) {
			break;
		}
		if(!judge1((int)operatorstack.top())) {
			error=0;
			logger.log(Level.INFO,"计算 "+expression+" 失败\n         原因：括号不匹配");
			break;
		}
		num1=numstack.top();float n1=num.top();	   
		numstack.pop();num.pop();	
		 float n2;
		try {
			num2=numstack.top();n2 = num.top();
		} catch (Exception e) {
			// TODO Auto-generated catch block		
			error=0;
		logger.log(Level.INFO,"计算 "+expression+" 失败\n         原因：运算符在最后一个");
			break;
		}	 
		 numstack.pop(); num.pop();	
		 operate=(int)operatorstack.top();
		 operatorstack.pop();
		 float r;
		 if(n1==n2&&n1==0) {
			  r=(int)result2((int)num1,(int)num2,operate);
			  num.push(0);
		 }
		 else {
			 r=result(num1,num2,operate);
			 num.push(1);
		 }
		 
		numstack.push(r);		
	}

	if(error==1){
		if(pan)	
			{
			logger.log(Level.INFO,"计算 "+expression+" 成功 \n         结果："+numstack.top());
			System.out.println(numstack.top());}
					else
						{
				logger.log(Level.INFO,"计算 "+expression+" 成功 \n         结果："+numstack.top());
						System.out.println((int)numstack.top());}
	}
}
}
static int result2(int a,int b,int p) {
int r=0;
switch(p) {
case'+':
	r=a+b;
	break;
case'-':
	r=b-a;
	break;
case'*':
	r=a*b;
	break;
case'/':
	r=b/a;
	break;
case'%':
	r=b%a;
	break;
}
return r;		
}
static float result(float a,float b,int p) {
float r=0;
switch(p) {
case'+':
	r=a+b;
	break;
case'-':
	r=b-a;
	break;
case'*':
	r=a*b;
	break;
case'/':
	r=b/a;
	break;
case'%':
	r=b%a;
	break;
}
return r;		
}
	
	public static void main(String[] args) {
		Logger logger=Logger.getLogger("表达式求值1.Main");
		Handler h = null;
		try {
			h=new FileHandler("d:\\java%u.log",true);
			h.setFormatter(new SimpleFormatter());
			logger.addHandler(h);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		Scanner sc=new Scanner(System.in);
		String str=sc.nextLine();
		String s=str.replaceAll(" ", "");
		Boolean pan=false;
		for(int i=0;i<s.length();i++) {
			if(s.charAt(i)=='.') {
				pan=true;
			}
			
		}
calculate(s,pan,logger);
	}

}
