% Read Images
imgA = imread("Moon.jpg");
imgB = imread("Sunflower.png");
% Convert to Grayscale
imgA = rgb2gray(imgA);
imgB = rgb2gray(imgB);
% Convert Images to Double
imgA = double(imgA);
imgB = double(imgB);
% Performing 2D FFT
fftA = fft2(imgA);
fftB = fft2(imgB);
% Shift the Output
fftAshift = fftshift(fftA);
fftBshift = fftshift(fftB);

% ----- Part-A -----
% Calculate Magnitude and Phase
magnitudeA = abs(fftAshift);
phaseA = angle(fftAshift);
magnitudeB = abs(fftBshift);
phaseB = angle(fftBshift);

% ----- Part-B -----
% Mixing Phases and Magnitudes
fftC = abs(fftA) .* exp(1i * angle(fftB));
fftD = abs(fftB) .* exp(1i * angle(fftA));
imgC = ifft2(fftC);
imgD = ifft2(fftD);
% Calculate Limits for Plotting
cmin = min(min(abs(imgC)));
cmax = max(max(abs(imgC)));
dmin = min(min(abs(imgD)));
dmax = max(max(abs(imgD)));

% ----- Part-C -----
figure
subplot(4,2,1);
imshow(uint8(imgA));
title('Image A: Moon');
subplot(4,2,2);
imshow(uint8(imgB));
title('Image B: Sunflower');
subplot(4,2,3);
imshow(log(1 + magnitudeA), []); colormap gray;
title('Image A Magnitude: Moon');
subplot(4,2,4);
imshow(phaseA);
title('Image A Phase: Moon');
subplot(4,2,5);
imshow(log(1 + magnitudeB), []); colormap gray;
title('Image B Magnitude: Sunflower');
subplot(4,2,6);
imshow(phaseB);
title('Image B Phase: Sunflower');
subplot(4,2,7);
imshow(abs(imgC), [cmin cmax]); colormap gray;
title('Image C: Magnitude A + phase B')
subplot(4,2,8);
imshow(abs(imgD), [dmin dmax]); colormap gray;
title('Image D: Magnitude B + phase A')