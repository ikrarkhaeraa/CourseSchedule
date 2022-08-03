package com.dicoding.courseschedule.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.sqlite.db.SupportSQLiteQuery
import com.dicoding.courseschedule.util.QueryType
import com.dicoding.courseschedule.util.QueryUtil
import com.dicoding.courseschedule.util.QueryUtil.nearestQuery
import com.dicoding.courseschedule.util.SortType
import com.dicoding.courseschedule.util.executeThread
import java.util.*

//TODO 4 : Implement repository with appropriate dao
class DataRepository(private val dao: CourseDao) {

    fun getNearestSchedule(queryType: QueryType) : LiveData<Course?> {
        val nearestQ: SupportSQLiteQuery = QueryUtil.nearestQuery(type = queryType)
        return dao.getNearestSchedule(nearestQ)
        throw NotImplementedError("needs implementation")
    }

    fun getAllCourse(sortType: SortType): LiveData<PagedList<Course>> {
        val sort = QueryUtil.sortedQuery(sortType)
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(PAGE_SIZE)
            .setPageSize(PAGE_SIZE)
            .build()
        return LivePagedListBuilder(dao.getAll(sort), config).build()
        throw NotImplementedError("needs implementation")
    }

    fun getCourse(id: Int) : LiveData<Course> {
        return dao.getCourse(id)
        throw NotImplementedError("needs implementation")
    }

    fun getTodaySchedule() : List<Course> {
        var day = GregorianCalendar().get(Calendar.DAY_OF_WEEK) - 1
        if (day == 0) {
            day = 7
        }
        return dao.getTodaySchedule(day)
        throw NotImplementedError("needs implementation")
    }

    fun insert(course: Course) = executeThread {
        dao.insert(course)
    }

    fun delete(course: Course) = executeThread {
        dao.delete(course)
    }

    companion object {
        @Volatile
        private var instance: DataRepository? = null
        private const val PAGE_SIZE = 10

        fun getInstance(context: Context): DataRepository? {
            return instance ?: synchronized(DataRepository::class.java) {
                if (instance == null) {
                    val database = CourseDatabase.getInstance(context)
                    instance = DataRepository(database.courseDao())
                }
                return instance
            }
        }
    }
}