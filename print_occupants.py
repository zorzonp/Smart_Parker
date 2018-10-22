import sqlite3

conn = sqlite3.connect('occupancy.db')
c = conn.cursor()

c.execute("SELECT * FROM times")
entries = c.fetchall()
conn.close()

if(len(entries) > 0):
    print("Number\tState\tIn\t\t\tOut")
    for item in entries:
        print("%s\t%s\t%10f\t%10f" % (item[0], item[1], item[2], item[3]))
else:
    print("Table is empty.")
