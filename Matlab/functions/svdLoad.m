function s = svdLoad(file)
%Loads SVD values from .dat files
%Input parameter is just the basename ie. it will append the .dat

    fileID = fopen(strcat([pwd,'/',file,'.dat']));
    tline = fgets(fileID);
    splited = strsplit(tline,' ');
    
    num = length(splited);
    s = [];
    for i = 1:num
        if(~isnan(str2double(splited(i))))
            s(i) = str2double(splited(i));
        end
    end
    
    fclose(fileID);
end