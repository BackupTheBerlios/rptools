FILES=$(wildcard *)

all:    beta

.PHONY: beta
.PHONY: live

beta: $(FILES)
	. .rsync beta

live: $(FILES)
	. .rsync
    
