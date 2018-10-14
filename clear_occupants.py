import sqlite3

conn = sqlite3.connect('occupancy.db')
c = conn.cursor()
c.execute("DELETE FROM times")
conn.commit()
conn.close()
