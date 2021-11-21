# Compound Poisson Beta Distribution

**Note: This code has not been thoroughly tested, and may contain errors.  It should be considered experimental.**

This code provides Implementations of cumulative distribution functions, 
survival functions, and occurrence exceedance probability calculations 
for the Beta distribution and Compound Poisson Beta distribution.  
The implementation is in Java, and uses the Apache Commons Math 
library for some underlying calculations, root solvers, etc.  

# Compilation notes

To build and run locally, you will need to install java, mvn, and the Apache Commons Math library.  
Once the necessary packages are installed, 
build with `make build` and run with `make run`.  

To use docker instead, run 
```
make build-docker
make run-docker
```

