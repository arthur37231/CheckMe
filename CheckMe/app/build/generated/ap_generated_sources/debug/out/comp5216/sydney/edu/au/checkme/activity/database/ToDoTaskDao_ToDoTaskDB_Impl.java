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
public final class ToDoTaskDao_ToDoTaskDB_Impl implements ToDoTaskDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ToDoTask> __insertionAdapterOfToDoTask;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public ToDoTaskDao_ToDoTaskDB_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfToDoTask = new EntityInsertionAdapter<ToDoTask>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `taskList` (`toDoTaskID`,`toDoTaskContent`) VALUES (nullif(?, 0),?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, ToDoTask value) {
        stmt.bindLong(1, value.getToDoTaskID());
        if (value.getToDoTaskContent() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getToDoTaskContent());
        }
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM taskList";
        return _query;
      }
    };
  }

  @Override
  public void insert(final ToDoTask toDoTask) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfToDoTask.insert(toDoTask);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertAll(final ToDoTask... toDoTasks) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfToDoTask.insert(toDoTasks);
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
  public List<ToDoTask> listAll() {
    final String _sql = "SELECT * FROM taskList";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfToDoTaskID = CursorUtil.getColumnIndexOrThrow(_cursor, "toDoTaskID");
      final int _cursorIndexOfToDoTaskContent = CursorUtil.getColumnIndexOrThrow(_cursor, "toDoTaskContent");
      final List<ToDoTask> _result = new ArrayList<ToDoTask>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final ToDoTask _item;
        final String _tmpToDoTaskContent;
        if (_cursor.isNull(_cursorIndexOfToDoTaskContent)) {
          _tmpToDoTaskContent = null;
        } else {
          _tmpToDoTaskContent = _cursor.getString(_cursorIndexOfToDoTaskContent);
        }
        _item = new ToDoTask(_tmpToDoTaskContent);
        final int _tmpToDoTaskID;
        _tmpToDoTaskID = _cursor.getInt(_cursorIndexOfToDoTaskID);
        _item.setToDoTaskID(_tmpToDoTaskID);
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
