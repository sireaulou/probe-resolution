clear;

sx = [.92,-.03,.05,-1.15,.23,.48,-.46];
sy = -[2.65,.13,.03,.68,.34,1.95,-2.78];

dx = [.3,-2.53,2.62,2.61];
dy = -[-2.04,.64,-.37,-.64];

plot(sx,sy,'.r','MarkerSize',15);
hold on 
plot(dx,dy,'.b','MarkerSize',15);
plot(-100,100,'k');
rectangle('Position',[-1 -1 2 2]);
hold off
daspect([1 1 1]);
axis([-5, 5, -5, 5]);
legend('Source','Detector','Phantom');

%NOT FINISHED YET
% Fittest 143
% x: [.92,-.03,.05,-1.15,.23,.48,-.46,.3,-2.53,2.62,2.61,];
% y: [2.65,.13,.03,.68,.34,1.95,-2.78,-2.04,.64,-.37,-.64,];
% 19.95