function numgrad = computeNumericalGradient(J, theta)
% numgrad = computeNumericalGradient(J, theta)
% theta: a vector of parameters
% J: a function that outputs a real-number. Calling y = J(theta) will return the
% function value at theta. 
  
% Initialize numgrad with zeros
numgrad = zeros(size(theta));

%% ---------- YOUR CODE HERE --------------------------------------
% Instructions: 
% Implement numerical gradient checking, and return the result in numgrad.  
% (See Section 2.3 of the lecture notes.)
% You should write code so that numgrad(i) is (the numerical approximation to) the 
% partial derivative of J with respect to the i-th input argument, evaluated at theta.  
% I.e., numgrad(i) should be the (approximately) the partial derivative of J with 
% respect to theta(i).
%                
% Hint: You will probably want to compute the elements of numgrad one at a time. 


basisfunc = zeros(size(theta));
epsilon = .0001;
fprintf('Going to compute numerical gradient\n');
for i = 1:numel(theta)
	basisfunc(i) = 1;
	sec_arg = J(theta - epsilon * basisfunc);
	first_arg = J(theta + epsilon * basisfunc);
	numgrad(i) = ( first_arg - sec_arg)/(2 * epsilon);
	basisfunc(i) = 0;
end



%% ---------------------------------------------------------------
end
