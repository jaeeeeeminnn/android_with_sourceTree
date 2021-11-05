package layout

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

/*  MyPagerAdapter

    viewPager에서 가질 수 있는 Adapter
    viewPager가 version 2로 업글되면서 변경점

    FragmentStatePagerAdapter / FragmentPagerAdapter -> FragmentStateAdapter

    로 타입이 변경됨.

    기존 버전의 타입을 쓸 경우에 main에서 adapter 타입이 import되지 않아, 빌드 수행이 불가하니 참고바람.

 */

class MyPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    // fragmnent list
    private val items = ArrayList<Fragment>()

    override fun getItemCount(): Int {
        return items.size
    }

    // 그냥 getItem() 이랑 같은 걸로 생각하자.
    // 안 쓰면 되지
    override fun createFragment(position: Int): Fragment {
        return items[position]
    }

    // updateFragments(List<Fragment>) : 외부에서 list 추가
    fun updateFragments(items : List<Fragment>) {
        this.items.addAll(items)
    }

}