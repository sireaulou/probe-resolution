%Reproduces Fig.3a from Culver's paper.
%Refer to my doc.

clear;
addpath('../functions');

%%refer to Culver's paper for obtaining alpha
% sigma = 0.001;
% S0 = 0.5;
% alpha = sqrt(1/numSD)*sqrt(numSD)*sigma/(S0/(6*6*0.02));
%%%%%%%%%%%%%
L = 60;
M = 45*45;
N = 900;
sigma = 0.001;
S01 = 0.5;
S02 = 0.05;
vol = 6*6*0.2;
alpha1 = sqrt(1/M)*(sigma*sqrt(M))/(S01/vol);
alpha2 = sqrt(1/M)*(sigma*sqrt(M))/(S02/vol);
% alpha1 = 0.12;
% alpha2 = alpha1*10;

for i=1:8
   s(i,:) = svdLoad(strcat('../../culverReproduction/culver3b/culver3b_',num2str(i)));
   s1(i,:) = svdLoad(strcat('../../culverReproduction/culver3b/culver3b_',num2str(i)));
   diff1 = abs(s(i,:)-alpha1);
   diff2 = abs(s1(i,:)-alpha2);
   [m1,ind1] = min(diff1);
   [m2,ind2] = min(diff2);
   res1(i)=2*L/ind1^(1/2);
   res2(i)=2*L/ind2^(1/2);
end

plot(2:2:16,res2,'.-');
hold on;
plot(2:2:16,res1,'-*');
hold off;
xlim([1,17]);
ylim([9,17]);
legend('Sig = 0.05','Sig = 0.5');
xlabel('Field of View (cm)');
ylabel('<res> (mm)');