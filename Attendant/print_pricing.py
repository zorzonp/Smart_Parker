import sqlite3

conn = sqlite3.connect('pricing.db')
c = conn.cursor()

c.execute("SELECT * FROM standard")
entries = c.fetchall()
conn.close()

if(len(entries) > 0):
    print("Time (h)\tPrice ($)")
    for item in entries:
        print("%2.1f\t\t$%4.2f" % (item[0], item[1]))
else:
    print("Table is empty.")
