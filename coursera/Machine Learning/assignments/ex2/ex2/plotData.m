function plotData(X, y)
%PLOTDATA Plots the data points X and y into a new figure 
%   PLOTDATA(x,y) plots the data points with + for the positive examples
%   and o for the negative examples. X is assumed to be a Mx2 matrix.

% Create New Figure
figure; hold on;

% ====================== YOUR CODE HERE ======================
% Instructions: Plot the positive and negative examples on a
%               2D plot, using the option 'k+' for the positive
%               examples and 'ko' for the negative examples.
%
 zeroy = find(y==0);
 onesy = find(y==1);
 plot(X(zeroy,1), X(zeroy,2), 'r+');
 plot(X(onesy,1), X(onesy,2), 'ko');
 title('University Admissions scenario');








% =========================================================================



hold off;

end
