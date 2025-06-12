% Read Image
circles = imread("Circles.jpg");
% Convert to GraySccale
circlesGS = rgb2gray(circles);
% Gaussian filter to reduce noise
smoothCircles = imgaussfilt(circlesGS,1);
% Laplacian edge detection (2nd order)
laplacianFilter = fspecial('laplacian', 0.2);
edges = imfilter(double(smoothCircles), laplacianFilter, 'replicate');
% imshow(edges);
binaryCircles = imfill(edges>2, 'holes');
% imshow(binaryCircles);
% Remove noise
binaryCircles = bwareaopen(binaryCircles,100);
% imshow(binaryCircles)
% 
[labeledCircles, DetectedCircles] = bwlabel(binaryCircles);
% Show the Result
figure;
imshow(circles);
title(['Total Number of Detected Circles: ', num2str(DetectedCircles)]);