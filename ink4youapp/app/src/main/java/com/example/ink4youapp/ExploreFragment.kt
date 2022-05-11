package com.example.ink4youapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ink4youapp.adapters.TatuadorDtoAdapter
import com.example.ink4youapp.adapters.TatuagemDtoAdpter
import com.example.ink4youapp.models.fakeTatuadores
import com.example.ink4youapp.models.fakeTatuagens
import android.system.Os.link
import android.widget.FrameLayout
import androidx.fragment.app.FragmentManager


class ExploreFragment : Fragment() {

    val fakeStyles = arrayOf("Estilo1", "Estilo2", "Estilo3", "Estilo4");

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.fragment_explore, container, false)

        val recyclerViewNewTattoos = view.findViewById<RecyclerView>(R.id.recyclerViewNewTattoos);
        recyclerViewNewTattoos.adapter = TatuagemDtoAdpter(fakeTatuagens());
        recyclerViewNewTattoos.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false);

        popularStyles();

        val recyclerViewNewTattoosArtists = view.findViewById<RecyclerView>(R.id.recyclerViewNewTattoosArtists);
        recyclerViewNewTattoosArtists.adapter = TatuadorDtoAdapter(fakeTatuadores());
        recyclerViewNewTattoosArtists.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false);

        return view;
    }

    fun popularStyles() {
        val fmStylesIds = arrayOf(R.id.fm_first_style, R.id.fm_second_style, R.id.fm_third_style, R.id.fm_fourth_style);

        var i = 0;
        while (i < fmStylesIds.size) {
            setPopularStyle(fmStylesIds[i], fakeStyles[i]);
            i++;
        }
        i = 0;
    }

    fun setPopularStyle(frame: Int, text: String) {
        var ft = childFragmentManager.beginTransaction();
        var styleFragment : Fragment = ExplorePopularStyleFragment();

        var bundle = Bundle();
        bundle.putString("style", text)
        styleFragment.setArguments(bundle);

        ft.add(frame, styleFragment, styleFragment.javaClass.name)
        ft.commitAllowingStateLoss();
    }

}