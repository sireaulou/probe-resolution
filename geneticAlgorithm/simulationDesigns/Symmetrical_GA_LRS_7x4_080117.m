clear;

sx = [3.67,-3.67,3.66,-3.66,3.67,-3.67,.0];
sy = -[.0,.0,-.02,-.02,.04,.04,-.5];

dx = [-.01,.01,-.01,.01];
dy = -[-1.5,-1.5,1.53,1.53];

plot(sx,sy,'.r','MarkerSize',15);
hold on 
plot(dx,dy,'.b','MarkerSize',15);
plot(-100,100,'k');
rectangle('Position',[-1 -1 2 2]);
hold off
daspect([1 1 1]);
axis([-5, 5, -3, 3]);
legend('Source','Detector','Phantom');

% Fittest 675
% x: [3.67,-3.67,3.66,-3.66,3.67,-3.67,.0,-.01,.01,-.01,.01,];
% y: [.0,.0,-.02,-.02,.04,.04,-.5,-1.5,-1.5,1.53,1.53,];
% 25.67