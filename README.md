# NTUMachineLearning
This is the Homeworks for Professor Hsuen-tien Lin's Machine Learning course.

##1. Naive PLA
Naive order PLA (Perceptron Learning Algorithm) implemented with JAVA.
PLA can do binary classification if the given training data D is linear separable. Otherwise the algorithm won't halt.
The PLA repeatedly check if the weighted sum (Wx) of each input has the same sign as the corresponding label, and will update the weights if different as following:W=W+yx.
The algorithm will halt once all the weighted sum of points in the input has the same sign as their corresponding label.
