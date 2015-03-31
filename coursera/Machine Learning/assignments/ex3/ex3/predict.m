function p = predict(Theta1, Theta2, X)
%PREDICT Predict the label of an input given a trained neural network
%   p = PREDICT(Theta1, Theta2, X) outputs the predicted label of X given the
%   trained weights of a neural network (Theta1, Theta2)

% Useful values
m = size(X, 1);
num_labels = size(Theta2, 1);
fprintf(' Size of Theta1: %f %f\n', size(Theta1, 1), size(Theta1, 2));
fprintf(' Size of Theta2: %f %f\n', size(Theta2, 1), size(Theta2, 2));
% You need to return the following variables correctly 
p = zeros(size(X, 1), 1);
X = [ones(m, 1), X];
fprintf(' Size of X: %f\n', size(X));
% ====================== YOUR CODE HERE ======================
% Instructions: Complete the following code to make predictions using
%               your learned neural network. You should set p to a 
%               vector containing labels between 1 to num_labels.
%
% Hint: The max function might come in useful. In particular, the max
%       function can also return the index of the max element, for more
%       information see 'help max'. If your examples are in rows, then, you
%       can use max(A, [], 2) to obtain the max for each row.
%
	hidden_layer = sigmoid(X * Theta1');
	hidden_layer = [ ones(size(hidden_layer, 1), 1), hidden_layer ];
	results = sigmoid(hidden_layer * Theta2');
	[max_num, p] = max( results, [], 2);


% =========================================================================


end
