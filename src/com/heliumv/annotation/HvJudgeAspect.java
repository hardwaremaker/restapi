/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2014 HELIUM V IT-Solutions GmbH
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published 
 * by the Free Software Foundation, either version 3 of theLicense, or 
 * (at your option) any later version.
 * 
 * According to sec. 7 of the GNU Affero General Public License, version 3, 
 * the terms of the AGPL are supplemented with the following terms:
 * 
 * "HELIUM V" and "HELIUM 5" are registered trademarks of 
 * HELIUM V IT-Solutions GmbH. The licensing of the program under the 
 * AGPL does not imply a trademark license. Therefore any rights, title and
 * interest in our trademarks remain entirely with us. If you want to propagate
 * modified versions of the Program under the name "HELIUM V" or "HELIUM 5",
 * you may only do so if you have a written permission by HELIUM V IT-Solutions 
 * GmbH (to acquire a permission please contact HELIUM V IT-Solutions
 * at trademark@heliumv.com).
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contact: developers@heliumv.com
 ******************************************************************************/
package com.heliumv.annotation;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.heliumv.factory.IJudgeCall;
import com.lp.util.EJBExceptionLP;

@Component
@Aspect
public class HvJudgeAspect extends BaseAspect {
	private static Logger log = LoggerFactory.getLogger(HvJudgeAspect.class) ;

	@Autowired
	private IJudgeCall judgeCall ;
	
    @Pointcut("execution(public * *(..))")
    private void anyPublicOperation() {}
    
    @Pointcut("within(com.heliumv..*)")  
    private void inHeliumv() {}
    
	@Pointcut("@annotation(com.heliumv.annotation.HvJudge)")
	public void processHvJudgePointcut() {
	}
	
//	@Before("processHvJudgePointcut()")
	@Before("anyPublicOperation() && inHeliumv() && @annotation(com.heliumv.annotation.HvJudge)")
	public void processAttribute(JoinPoint pjp) throws Throwable {
//		Class clazz = pjp.getSignature().getDeclaringType() ;
//		List<HvJudge> attributesList = new ArrayList<HvJudge>() ;
//	
//		System.out.println("In declaring clazz " + clazz.getName() + " inspecting annotations...") ;
		

	    MethodSignature methodSig = getMethodSignatureFrom(pjp) ;
	    Method method = getMethodFrom(pjp) ;
		HvJudge theModul = method.getAnnotation(HvJudge.class);
		if(theModul == null) return ;
		
//		System.out.println("Having the HvJudge Annotation with name |'" + theModul.recht() + "'|" +
//				StringUtils.join(theModul.rechtOder(), "|") + "<") ;
		log.debug("Having the HvJudge Annotation with name |'" + theModul.recht() + "'|" +
				StringUtils.join(theModul.rechtOder(), "|") + "<") ;
	
		if(theModul.recht().length() > 0) {
			if(!judgeCall.hatRecht(theModul.recht())) {
				log.info("judge disallowed: '" + theModul.recht() + "' for '" + methodSig.getMethod().getName() +"'") ;
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_UNZUREICHENDE_RECHTE,
						methodSig.getMethod().getName() + ":" + theModul.recht() ) ;
			}

//			System.out.println("judge allowed: '" + theModul.recht() + "'") ;
			log.debug("judge allowed: '" + theModul.recht() + "'") ;
			return ;
		}
		
		if(theModul.rechtOder().length > 0) {
			for (String recht : theModul.rechtOder()) {
				if(judgeCall.hatRecht(recht)) {
					log.debug("judge allowed: '" + recht + "'") ;
//					System.out.println("judge allowed: '" + recht + "'") ;
					return ;					
				}
			}

			log.info("judge disallowed: '" + Arrays.toString(theModul.rechtOder()) + "' for '" + methodSig.getMethod().getName() +"'") ;
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_UNZUREICHENDE_RECHTE,
					methodSig.getMethod().getName() + ":" + Arrays.toString(theModul.rechtOder())) ;
		}
	}
}
