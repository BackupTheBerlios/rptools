FILES=$(shell find src -type f)
DIRS=$(shell find src -type d)

DIRMODE=0755
FILEMODE=0644


noneed:
	@echo "No need to push to the web server, cvs updates are done automatically"

beta:   noneed

live:   noneed

all:    noneed


.PHONY: all
.PHONY: beta
.PHONY: live
.PHONY: permissions
.PHONY: noneed
.PHONY: forcebeta
.PHONY: forcelive

permissions:
	@echo "Fixing permissions..."
	@chmod $(FILEMODE) $(FILES)
	@chmod $(DIRMODE) $(DIRS)


forcebeta: $(FILES) permissions
	./.rsync beta

forcelive: $(FILES) permissions
	./.rsync
    
