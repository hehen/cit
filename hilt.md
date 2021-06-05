Hilt提供了整合Dagger依赖注入到Android应用程序中的标准方法。
使用Hilt的目的：
Hilt会生成Dagger代码，避免手动编写样板代码，只需关注对象创建和注入

错误: Dagger does not support injection into private fields
 @HiltViewModel annotated class should contain exactly one @Inject annotated constructor.
 FragmentC repeats modules with scoped bindings or declarations
 dagger.hilt.android.internal.managers.ViewComponentManager$FragmentContextWrapper cannot be cast to androidx.fragment.app.FragmentActivity