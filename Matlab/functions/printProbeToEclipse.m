function printProbeToEclipse(x,y,numSource,numDetector,probeName)
    for i=1:numSource
        disp([probeName,'.addSource(',num2str(x(i),'%.2f'),',',num2str(y(i),'%.2f'),',0);']); 
    end

    for i=numSource+1:(numDetector+numSource)
       disp([probeName,'.addDetector(',num2str(x(i),'%.2f'),',',num2str(y(i),'%.2f'),',0);']); 
    end 
end