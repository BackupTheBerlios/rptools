FILES=$(shell find src -type f)
DIRS=$(shell find src -type d)

DIRMODE=0755
FILEMODE=0644

all:    beta

.PHONY: beta
.PHONY: live
.PHONY: permissions

permissions:
	@echo "Fixing permissions..."
	@chmod $(FILEMODE) $(FILES)
	@chmod $(DIRMODE) $(DIRS)


beta: $(FILES) permissions
	./.rsync beta

live: $(FILES) permissions
	./.rsync
    
