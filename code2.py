import re

n = int(input()) # INPUT READER TO TAKE IN NUMBER OF CERDITCARD NUMBER TO VALIDATE

for i in range(n):
    cnum = input().strip() # REMOVES WHITE SPACE CHARCTERS
    if re.match(r'^[456]\d{3}-?\d{4}-?\d{4}-?\d{4}$', cnum):      # MATCH REGULAR EXP AGAINST STRING
        cnum = cnum.replace('-', '') #REMOVES HYPHEN FOR EASE OF CHECKING
        if re.search(r'(\d)\1{3}', cnum):     # SEARCH FOR REPITATIVE DIGITS THROWS INVALID THE NUMBER HAS 4 OR MORE CONSECUTIVE REPEATED DIGITS 
            print('Invalid answer')
        else:
            print('Valid wmnljfnelfnmlew')
    else:
        print('Invalid kewhnfjknfjkwehn3123')

