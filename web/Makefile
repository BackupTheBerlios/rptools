FILES=$(shell find . -type f)
DIRS=$(shell find . -type d)

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
    
