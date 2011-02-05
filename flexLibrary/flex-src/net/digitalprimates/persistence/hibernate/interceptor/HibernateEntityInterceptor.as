/**
 * Copyright (c) 2011 Digital Primates
 * 
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 * 
 * @author     Michael Labriola 
 * @version    
 **/
package net.digitalprimates.persistence.hibernate.interceptor {
	
	import net.digitalprimates.persistence.entity.IEntityObjectManager;
	import net.digitalprimates.persistence.hibernate.constants.HibernateConstants;
	import net.digitalprimates.persistence.hibernate.IHibernateManagedEntity;
	
	import org.as3commons.bytecode.interception.IMethodInvocationInterceptor;
	import org.as3commons.bytecode.interception.impl.InvocationKind;
	
	public class HibernateEntityInterceptor implements IMethodInvocationInterceptor {
		private var objectManager:IEntityObjectManager;

		public function HibernateEntityInterceptor( objectManager:IEntityObjectManager ) {
			this.objectManager = objectManager;
		}
		
		final public function intercept(targetInstance:Object, kind:InvocationKind, member:QName, arguments:Array=null, method:Function=null):* {
			var targetEntity:IHibernateManagedEntity = targetInstance as IHibernateManagedEntity;
			
			switch (kind) {
/*				
				We are no longer proxying methods at this point

				case InvocationKind.CONSTRUCTOR:
					break;
				case InvocationKind.METHOD:
					return method.apply(targetInstance, arguments);
					break;
*/				case InvocationKind.GETTER:
					if ( !targetEntity ) {
						return arguments[0];
					}

					if ( ( targetEntity.proxyInitialized ) || 
						 ( targetEntity.comStatus & HibernateConstants.PENDING ) || 
						 ( targetEntity.comStatus & HibernateConstants.SERIALIZING ) ) {
						return arguments[0];						
					} else {						
						objectManager.getProperty( targetEntity, member.localName );
						return null;
					}
					
					break;
				case InvocationKind.SETTER:
					return arguments[0];
					break;
				default:
					break;
			}

		}
	}
}