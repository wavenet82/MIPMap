/* Copyright 2015-2016 by the Athens University of Economics and Business (AUEB).

   This file is part of MIPMap.

   MIPMap is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    MIPMap is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with MIPMap.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.nfunk.jep.function;

//giannisk

import java.util.Stack;
import org.nfunk.jep.ParseException;

public class Contains  extends PostfixMathCommand{
  
    public Contains()
     {
         numberOfParameters = 2;
     }
    
    public void run(Stack inStack) throws ParseException 
	{
            checkStack(inStack);// check the stack
            Object param2 = inStack.pop();
            Object param1 = inStack.pop();
            inStack.push(param1.toString().contains(param2.toString()));//push the result on the inStack
            return; 
	} 
    
}
