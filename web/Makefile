FILES=$(shell find src -type f)
DIRS=$(shell find src -type d)

DIRMODE=0755
FILEMODE=0644

all:    beta

.PHONY: beta
.PHONY: live

beta: $(FILES)
	chmod $(FILEMODE) $(FILES)
	chmod $(DIRMODE) $(DIRS)
	. .rsync beta

live: $(FILES)
	chmod $(FILEMODE) $(FILES)
	chmod $(DIRMODE) $(DIRS)
	. .rsync
    
