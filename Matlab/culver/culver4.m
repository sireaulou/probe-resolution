%Reproduces Fig.4 from Culver's paper.
%Refer to my doc.

clear;
addpath('../functions');

L = 60; %length
M = 30*30; %number of measurements
N = 30; %number of voxels
%variables based on the paper
sigma = 0.00001; 
S01 = 0.5;
vol = 6*6*0.2;
alpha1 = sqrt(1/M)*(sigma*sqrt(M))/(S01/vol);


s = [];
res1 = [];
res2 = [];
for i=1:12
   s(i,:) = svdLoad(strcat('../../culverReproduction/tenthCulver4_',num2str(i)));
   diff1 = abs(s(i,:)-alpha1);
   [m1,ind1] = min(diff1);
   res1(i)=2*L/ind1;
end

plot(0.8:0.2:3,res1,'-d');
xlim([0.8,3]);
ylim([4,14]);
set(gca,'YTick', 4:2:14);
xlabel('Depth(cm)');
ylabel('<res> (mm)');
legend('Transmission');