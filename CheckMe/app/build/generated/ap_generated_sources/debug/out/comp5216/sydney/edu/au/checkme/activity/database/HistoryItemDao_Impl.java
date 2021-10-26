package comp5216.sydney.edu.au.checkme.activity.database;

import android.database.Cursor;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class HistoryItemDao_Impl implements HistoryItemDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<HistoryItem> __insertionAdapterOfHistoryItem;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public HistoryItemDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfHistoryItem = new EntityInsertionAdapter<HistoryItem>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `HistoryItemList` (`ID`,`eventId`,`eventName`,`visitingTime`,`eventAddr`,`riskLevel`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, HistoryItem value) {
        stmt.bindLong(1, value.getID());
        if (value.getEventId() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getEventId());
        }
        if (value.getEventName() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getEventName());
        }
        if (value.getVisitingTime() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getVisitingTime());
        }
        if (value.getEventAddr() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getEventAddr());
        }
        if (value.getRiskLevel() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getRiskLevel());
        }
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM historyitemlist";
        return _query;
      }
    };
  }

  @Override
  public void insert(final HistoryItem item) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfHistoryItem.insert(item);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteAll() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteAll.release(_stmt);
    }
  }

  @Override
  public List<HistoryItem> listAll() {
    final String _sql = "SELECT * FROM historyitemlist ORDER BY ID DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfID = CursorUtil.getColumnIndexOrThrow(_cursor, "ID");
      final int _cursorIndexOfEventId = CursorUtil.getColumnIndexOrThrow(_cursor, "eventId");
      final int _cursorIndexOfEventName = CursorUtil.getColumnIndexOrThrow(_cursor, "eventName");
      final int _cursorIndexOfVisitingTime = CursorUtil.getColumnIndexOrThrow(_cursor, "visitingTime");
      final int _cursorIndexOfEventAddr = CursorUtil.getColumnIndexOrThrow(_cursor, "eventAddr");
      final int _cursorIndexOfRiskLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "riskLevel");
      final List<HistoryItem> _result = new ArrayList<HistoryItem>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final HistoryItem _item;
        final String _tmpEventId;
        if (_cursor.isNull(_cursorIndexOfEventId)) {
          _tmpEventId = null;
        } else {
          _tmpEventId = _cursor.getString(_cursorIndexOfEventId);
        }
        final String _tmpEventName;
        if (_cursor.isNull(_cursorIndexOfEventName)) {
          _tmpEventName = null;
        } else {
          _tmpEventName = _cursor.getString(_cursorIndexOfEventName);
        }
        final String _tmpVisitingTime;
        if (_cursor.isNull(_cursorIndexOfVisitingTime)) {
          _tmpVisitingTime = null;
        } else {
          _tmpVisitingTime = _cursor.getString(_cursorIndexOfVisitingTime);
        }
        final String _tmpEventAddr;
        if (_cursor.isNull(_cursorIndexOfEventAddr)) {
          _tmpEventAddr = null;
        } else {
          _tmpEventAddr = _cursor.getString(_cursorIndexOfEventAddr);
        }
        final String _tmpRiskLevel;
        if (_cursor.isNull(_cursorIndexOfRiskLevel)) {
          _tmpRiskLevel = null;
        } else {
          _tmpRiskLevel = _cursor.getString(_cursorIndexOfRiskLevel);
        }
        _item = new HistoryItem(_tmpEventId,_tmpEventName,_tmpVisitingTime,_tmpEventAddr,_tmpRiskLevel);
        final int _tmpID;
        _tmpID = _cursor.getInt(_cursorIndexOfID);
        _item.setID(_tmpID);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
